# Setup instructions

Notes
-----

- Instructions for installation on Debian 7 GNU/Linux
- uncommented lines are system commands to run
- all commands to be ran in this directory

1. System dependencies

    1.1 Install Oracle Java 7
        echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee /etc/apt/sources.list.d/webupd8team-java.list
        echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list
        apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886
        apt-get update
        apt-get install oracle-java7-installer

    1.2. Install MySQL (RDBMS), MCL (markov clustering), Maven
        apt-get install mysql mcl maven

2. Database configuration

    2.1 Log into the mysql shell as a root user
        mysql -u root -p
    2.2 Create database user, database and grant privileges (type into mysql shell)
        CREATE DATABASE cs261;
        CREATE USER 'cs261'@'localhost' IDENTIFIED BY '<password>';
        GRANT ALL PRIVILEGES ON cs261.* TO 'cs261'@'localhost';
    2.3 Import schema and procedures into MySQL
        cat sql/schema/*.sql | mysql -u cs261 -p
        cat sql/procedures/*.sql | mysql -u cs261 -p

3. Setup config file (~/fraud/config)

    3.1. Copy sample configuration file
        mkdir ~/fraud
        cp fraud.config.sample ~/fraud/config

    3.2. Set properties to match database parameters:
        nano ~/fraud/config
        jdbc.url=jdbc:mysql://localhost:3306/<database>
        jdbc.username=<username>
        jdbc.password=<password>

4. Maven

    4.1 Install to local Maven repository
        mvn clean install

5. Run FRAuD Tool
    
    5.1 run frontend (requires root access to bind on port 80)
        sudo frontend.sh
    5.2 run backend
        ./backend.sh