all: pre

pre:
	sudo apt-get update	
	sudo apt install default-jdk --assume-yes
	./install.sh




