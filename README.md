# Anypoint Platform KPI Dashboard

## How to run 
```
git clone git@github.com:dhimate/ap-dashboard.git
cd ap-dashboard
./mvnw clean package -Dapi.user.name={anypoint_platform_username} -Dapi.user.password={anypoint_platform_user_password}
java -jar target/ap-dashboard-0.0.1-SNAPSHOT.jar --api.user.name={anypoint_platform_username} --api.user.password={anypoint_platform_password}

```
