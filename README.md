# demo-ansattporten-integration
This is a very simple application that demonstrates an ansattporten integration, including log in and selecting a representation on behalf of a company.
  
  
  
## To run application
Set Environment variable clientSecret to the ansattporten client secret.  
If needed, update your client information in the application profile.

If you're testing this, and don't have an ansattporten client, please contact digdir to enquire for one,  
you can find more details in the [Ansattporten integration guide](https://docs.digdir.no/docs/idporten/oidc/ansattporten_guide)

## Web stack alternatives
The application on main branch uses WebFlux and Netty, creating a reactive stack, through spring-boot-starter-webflux.  
The application in the "tomcat" branch uses a Servlet stack and tomcat, through spring-boot-starter-web
To read a little more about the differences: [spring-web vs spring-webflux](https://medium.com/@burakkocakeu/spring-web-vs-spring-webflux-9224260c47b5)