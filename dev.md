# Start database
```
docker run --ulimit memlock=-1:-1 -it --rm=true --memory-swappiness=0 \
    -e POSTGRES_PASSWORD=passme -p 5432:5432 postgres:12.2
```

# Start voting client
```
npm run --prefix ./voting-client serve -- --port 8081
```

# Start music client
```
npm run --prefix ./musicclient serve -- --port 8084
```

# Start qr
```
google-chrome joinpage/index.html
```

# Start quarkus
```
./mvnw compile quarkus:dev
```

# Note: 
Everybody needs his own API-Key (Google Location Security).
