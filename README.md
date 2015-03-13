# The following installation instructions have been written
# for Debian 7 GNU/Linux

# 1) java
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
apt-get update
apt-get install oracle-java7-installer

# 2_ mysql (RDBMS), mcl (markov clustering), maven
apt-get install mysql mcl maven

# 3) log into the mysql shell as a root user, then set up database
mysql -u root -p
# CREATE DATABASE cs261;
# CREATE USER 'cs261'@'localhost' IDENTIFIED BY '<password>';
# GRANT ALL PRIVILEGES ON cs261.* TO 'cs261'@'localhost';

# 4) import schema and procedures into MySQL
cat sql/schema/*.sql | mysql -u cs261 -p
cat sql/procedures/*.sql | mysql -u cs261 -p

# 5) set up config file
mkdir ~/fraud
cp fraud.config.sample ~/fraud/config
# edit jdbc.url, jdbc.username and jdbc.password
# entries to match database parameters

# 6) install to maven
# configure common/pom.xml, edit jdbcUrl, jdbcUsername and jdbcPassword tags to match database settings
mvn clean install

# 7) run frontend (requires root access to bind on port 80)
sudo frontend.sh
# 8) run backend
./backend.sh
