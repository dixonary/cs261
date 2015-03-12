# The following installation instructions have been written
# for Debian 7 GNU/Linux

# java
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
apt-get update
apt-get install oracle-java7-installer

# mysql (RDBMS), mcl (markov clustering), maven
apt-get install mysql mcl maven

# log into the mysql shell as a root user, then set up database
mysql -u root -p
# CREATE DATABASE cs261;
# CREATE USER 'cs261'@'localhost' IDENTIFIED BY '<password>';
# GRANT ALL PRIVILEGSES ON cs261.* TO 'cs261'@'localhost';

# set up config file
mkdir ~/fraud
cp fraud.config.sample ~/fraud/config
# edit jdbc.url, jdbc.username and jdbc.password
# entries to match database parameters

# install backend
mvn clean install

# run frontend
sudo frontend.sh
# run backend
./backend.sh
