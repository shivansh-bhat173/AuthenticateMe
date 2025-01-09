# Authentication Services

A springBoot application that provide with options like sign and login.
Will be able to signup and store to DB using JPA and Hibernate.
Every time a user is signedup, a JWT(short lived) and a refresh token will be generated, and stored in the DB, 
and local storage which enables the user to not login again. But when he refresh token expires, redirects to the login Page

