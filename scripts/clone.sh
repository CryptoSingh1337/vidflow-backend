#!/bin/bash -

DIR="vidflow-backend/"
REPO_URL="https://github.com/CryptoSingh1337/vidflow-backend"
if [ -d "$DIR" ]
then
        rm -rf $DIR
        echo "############# Deleted the existing repo #############"
fi

git clone $REPO_URL
echo "############# Cloned the vidflow-backend repo #############"