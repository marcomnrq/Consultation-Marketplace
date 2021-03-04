package com.marcomnrq.consultation.service;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.marcomnrq.consultation.domain.model.Device;
import com.marcomnrq.consultation.domain.model.NotificationEmail;
import com.marcomnrq.consultation.domain.model.User;
import com.marcomnrq.consultation.domain.repository.DeviceRepository;
import com.marcomnrq.consultation.domain.repository.UserRepository;
import com.marcomnrq.consultation.exception.CustomException;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class SentinelService {

    // TODO: security check and implement more security features

    private final LoadingCache<String, Integer> attemptsCache =
            CacheBuilder.newBuilder().expireAfterWrite(24, TimeUnit.HOURS).build(
                    new CacheLoader<String, Integer>() {
                        public Integer load(String remoteAddress) { return 0; }
    });

    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final MailService mailService;

    //private final File file = new File("resources/GeoLite2-City.mmdb");

    private final Parser parser;

    /*
        CALLED EVENT FUNCTIONS
    */
    public void loginFailed(String remoteAddress) {
        bruteForceAttempt(remoteAddress);
        throw new CustomException("Sentinel Service: Bad Credentials", HttpStatus.FORBIDDEN );
    }
    public void loginSucceeded(String remoteAddress) {
        bruteForceInvalidate(remoteAddress);
        System.out.println("Login succeeded: " + remoteAddress);

    }
    /*
        LOGIN NOTIFICATION
    */
    public void verifyDevice(Authentication authentication, HttpServletRequest request) {
        String username;
        System.out.println("-------------------------------");
        System.out.println("AUTHENTICATION LOGIN");
        if (authentication.getPrincipal() instanceof User) {
            username = ((User)authentication.getPrincipal()).getEmail();
        }
        else {
            username = authentication.getName();
        }
        System.out.println("Email: " + username);
        System.out.println("Address: " + request.getRemoteAddr());
        System.out.println("-------------------------------");
        verifyDevice(userRepository.findByEmail(username).orElseThrow(()->new CustomException("invalid user")), request);
    }
    public void verifyDevice(User user, HttpServletRequest request) {
        String ip = getClientIP(request);
        String location = getIpLocation(ip);
        String deviceDetails = getDeviceDetails(request.getHeader("user-agent"));
        Optional<Device> optionalDevice = deviceRepository.findByUserAndDeviceDetailsAndLocation(user, deviceDetails, location);
        if(optionalDevice.isEmpty()){
            // New device login
            System.out.println("------- NEW DEVICE DETECTED ----------");
            Device device = new Device();
            device.setUser(user);
            device.setLocation(location);
            device.setDeviceDetails(deviceDetails);
            deviceRepository.save(device);

            // TODO: send notification email
            NotificationEmail notificationEmail = new NotificationEmail();
            notificationEmail.setRecipient(user.getEmail());
            notificationEmail.setSubject("Inicio de sesión en dispositivo nuevo");
            notificationEmail.setFullName(user.getFirstName());
            notificationEmail.setButtonLink("https://google.com");
            notificationEmail.setButtonText("Ver ajustes de dispositivos");
            notificationEmail.setBody("Hemos detectado que has iniciado sesión en un dispositivo nuevo. Si no fuiste tu, es importante tomar acción inmediata. Los detalles del dispositivo: " + deviceDetails);
            mailService.sendMail(notificationEmail);
        }
    }
    /*
        BRUTE FORCE DETECTION
    */
    public void bruteForceAttempt(String remoteAddress){
        int attempts = 0;
        try {
            attempts = attemptsCache.get(remoteAddress);
        } catch (ExecutionException e) {
            throw new CustomException("Sentinel Service: Unexpected error", HttpStatus.FORBIDDEN);
        }
        attempts++;
        attemptsCache.put(remoteAddress, attempts);
    }
    public void bruteForceInvalidate(String remoteAddress){
        attemptsCache.invalidate(remoteAddress);
    }
    public void bruteForceCheck(HttpServletRequest request) {
        try {
            int MAX_ATTEMPT = 10;
            if (attemptsCache.get(getClientIP(request)) >= MAX_ATTEMPT){
                throw new CustomException("Sentinel Service: Client blocked", HttpStatus.FORBIDDEN);
            }
        } catch (ExecutionException e) {
            throw new CustomException("Sentinel Service: Unexpected error", HttpStatus.FORBIDDEN);
        }
    }
    /*
        SUSPICIOUS AUTHENTICATION
    */
    private void checkSuspiciousAuth(User user){

    }

    /*
        UTILITY FUNCTIONS
    */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        // return "128.101.101.101"; for testing United States
        // return "41.238.0.198"; for testing Egypt
        return xfHeader.split(",")[0];
    }

    private String getIpLocation(String ip) {
        String location = "UNKNOWN";
        /*

        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        CityResponse cityResponse = null;
        try {
            cityResponse = databaseReader.city(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(cityResponse) &&
                Objects.nonNull(cityResponse.getCity()) &&
                !Strings.isNullOrEmpty(cityResponse.getCity().getName())) {
            location = cityResponse.getCity().getName();
        }
         */
        return location;
    }

    private String getDeviceDetails(String userAgent) {
        String deviceDetails = "UNKNOWN";
        Client client = parser.parse(userAgent);
        if (Objects.nonNull(client)) {
            System.out.println("!! CLIENT DETAILS: "+ client);
            deviceDetails = client.userAgent.family
                    + " " + client.userAgent.major + "."
                    + client.userAgent.minor + " - "
                    + client.os.family + " " + client.os.major
                    + "." + client.os.minor;
        }
        return deviceDetails;
    }

}
