Important: change application.properties file and properly set up your own local postgres database.

Auth flow that this API's security is based on goes like this -->

Client logs in with username and password and receive access and refresh tokens.

Client uses access token for making multiple requests.
When access token expires, client uses refresh token to retrieve a new pair of tokens.
Client uses the new access token to make API request.
