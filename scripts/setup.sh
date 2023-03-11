#!/bin/bash -

sudo apt update
sudo apt install openjdk-17-jdk-headless -y
sudo apt install nginx -y
sudo apt install maven -y

sudo systemctl start nginx
sudo systemctl enable nginx

sudo cp nginx/default /etc/nginx/sites-available/default
sudo mv /var/www/html/index.nginx-debian.html /var/www/html/index.html
sudo nginx -t

sudo cp build.sh $HOME/build.sh
sudo cp clone.sh $HOME/clone.sh
sudo cp start $HOME/start

chmod +x $HOME/build.sh
chmod +x $HOME/clone.sh
chmod +x $HOME/start

/bin/bash $HOME/clone.sh
/bin/bash $HOME/build.sh

sudo cp vidflow.service /etc/systemd/system/vidflow.service
sudo systemctl start vidflow.service
sudo systemctl enable vidflow.service
