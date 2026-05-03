# Build stage: We use a Maven image to build the .war file
FROM maven:3.9.6-eclipse-temurin-11 AS build
WORKDIR /app
# First copy only pom.xml to cache the dependencies download step
COPY WomenSafetyApp/pom.xml .
# Download dependencies (this will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B
# Now copy the source code
COPY WomenSafetyApp/src ./src
# Build the WAR file without running tests
RUN mvn clean package -DskipTests

# Run stage: We use a standard Tomcat image to serve the application
FROM tomcat:9
# Copy the built war file from the build stage into Tomcat's webapps folder
# Renaming it to ROOT.war ensures the app loads at the main domain
COPY --from=build /app/target/WomenSafetyApp.war /usr/local/tomcat/webapps/ROOT.war

# Expose port 8080 (the default port for Tomcat)
EXPOSE 8080

# Start the Tomcat server
CMD ["catalina.sh", "run"]
