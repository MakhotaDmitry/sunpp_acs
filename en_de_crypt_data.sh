#!/bin/bash


if [ "$1" == "--encrypt" ]; then
    tar cz data | openssl enc -aes-256-cbc -e > data.tar.gz.enc
#     tar cz db.db | openssl enc -aes-256-cbc -e > db.db.tar.gz.enc
elif [ "$1" == "--decrypt" ]; then
    openssl enc -aes-256-cbc -d -in data.tar.gz.enc | tar xz
#     openssl enc -aes-256-cbc -d -in db.db.tar.gz.enc | tar xz
else
    echo "Error udefined parameter"
    echo "--encrypt OR --decrypt ?"
fi
