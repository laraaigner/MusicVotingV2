echo ip-address of this computer:
read host
echo password for admin:
read password
echo WLAN SSID:
read ssid
echo WLAN password:
read wlan_pwd

# set environment variables
echo "password=$password" > server/src/main/resources/config.properties
sed "s/%%%HOST%%%/$host/g" templates/env.template > musicclient/.env
sed "s/%%%HOST%%%/$host/g" templates/env.template > voting-client/.env

# create join page from template
mkdir -p joinpage
sed "s/%%%HOST%%%/$host/g; s/%%%SSID%%%/$ssid/g; s/%%%WLAN_PWD%%%/$wlan_pwd/g"  templates/index.html.template > joinpage/index.html

# compile server
cd server/
mvn clean install
cd ..

docker-compose build
docker-compose up -d
docker-compose logs
