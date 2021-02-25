# Consultation Marketplace
This project is a consultation marketplace that connects professionals with customers, all
in a single place. Professionals can create different listings with their contact information
for clients to get in touch in order to acquire their services. The project has been made using
Spring Boot (Java) for the API and Vue.js for the frontend.

## Live Demo
You can check out the live demo at: https://consultation-marketplace.herokuapp.com/. 
The project has been deployed with the cloud platform Heroku. The production branch
is the 'main' branch of this repository.

## Authentication & Authorization
Authentication has been implemented with Spring Security using JWT. The expiration time for
every token is 90 seconds, you can refresh your token with the endpoint: ".../api/v1/authen
tication/refresh-token". Authorization is handled with user roles, which can be user,
professional or admin. Lastly, is worth to mention that users can enable 2FA on their accounts
and add an extra layer of security.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.


