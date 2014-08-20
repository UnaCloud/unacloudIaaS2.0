UnaCloud 2.0
========
Opportunistic Cloud Computing Infrastructure as a Service
# Introduction
UnaCloud is a project developed by the investigation group COMIT (Comunicaciones y Tecnología de Información) from Universidad de los Andes, Colombia. UnaCloud is an opportunistic cloud computing Infrastructure as a Service (IaaS) model implementation, which provides at lower cost basic computing resources (processing, storage and networking) to run arbitrary software, including operating systems and applications. The IaaS model is provided through the opportunistic use of idle computing resources available in the university campus. 
UnaCloud deals with the problems associated to use commodity, non-dedicated, distributed, and heterogeneous computing resources that are part of different administrative domains. We propose an IaaS architecture based on two strategies: an opportunistic strategy that allows the use of idle computing resources in a nonintrusive manner, and a virtualization strategy to allow the on demand deployment of customized execution environments.
# Installation Guide
This guide allows you to install UnaCloud 2.0 on your infrastructure.
## Requirements

For UnaCloud Server:
* A 4GB and 2 cores machine
* Windows Server 2003 or Windows 7 SO.
* 1 GB for UnaCloud Server and at least 80 GB of hard disk for Virtual Machines
* Java JDK SE 7

For UnaCloud Agents:
* At least 200 MB of free RAM.
* 50 MB for UnaCloud client and at least 20 GB of hard disk for Virtual Machines.
* Java JRE SE 7
* At least one of the following hypervisors:
* VMware Player 7, VMware Workstation 6 or better (if you use VMWare Player, you must install VMware Player and VMware VIX together)
*Oracle VM VirtualBox 4.2.14 or better.

##Unacloud WAR Download
In order to download unacloud war, you can access to http://unacloud.uniandes.edu.co/warconfigurer/. You must have a mySQL DB with an empty schema already configured. In this site you’ll enter the database configuration, and the admin password. Then you will be able to download the server WAR file.
## UnaCloud Server Deployment
You can deploy the WAR file with Apache Tomcat 8.0 (preferred), or a server of your choice. You can go to http://tomcat.apache.org/tomcat-8.0-doc/deployer-howto.html  for more information about deployment.

Please be careful with Java enviromental variables configuration.
##UnaCloud Agent Deployment
In order to deploy UnaCloud Agent in your infrastructure, you must download its files. The agent files could be downloaded from the web app route “/UnaCloudServices/updater” or simply the "Download Agent Files" button under "Configuration". Up next, you must decompress the zip file and copy the files contained in each agent machine. For each machine in your infrastructure, copy the uncompressed content in a directory of your choice. 

Now you must open a console and configure the local variables executing the client configuration program (“java –jar ClientConfigurer.jar”). This program will ask for the machine hostname (LOCAL_HOSTNAME, name used by the agent to identify itself against the server), VMWare “vmrun.exe” location (VMRUN_DIRECTORY, found on “C:\Program Files\VMware\VMware VIX\vmrun.exe” as default), VirtualBox “VBoxManage.exe” location (VBOX_DIRECTORY, found on “C:\Program Files\Oracle\VirtualBox\VBoxManage.exe” as default) and repository path (VM_REPO_PATH, directory used to copy and save the virtual machines) variables. You require to configure at least one hypervisor path and the repository location. If you don’t insert a specific hostname, the agent will use the machine hostname.

Finally, when the configuration process is finished, you must add the ClientUpdater.jar file as a boot script, following these steps:
*	Create a text file like the next one that will include commands in order to change the path and execute the client updater jar. Save it as a .bat file. 

> ####startUnacloud.bat

> cd C:\UnaCloud\

> java –jar ClientUpdater.jar 1

*	Open the Local Group Policy Editor (Type gpedit.msc from the start menu)
*	In the console tree, click Scripts (Startup/Shutdown). The path is Computer Configuration\Windows Settings\Scripts (Startup/Shutdown) 
*	Click Startup and then Add…
*	Insert the path of your .bat file on script name.
*	Click ok and then ok. The next time that you restart the machine, it will start with UnaCloud Agent.

#UnaCloud server configuration guide
## Creating Physical Laboratories
UnaCloud sorts Physical machines by Laboratories for GUI convenience. By default, a Laboratory is created in the database to add physical machines to it. If you need to change or create more laboratories you must do it directly in the database. A physical machine is a desktop or dedicated machine that has UnaCloud agent installed and is used to deploy opportunistic virtual machines. Each physical machine is associated to a Laboratory. In order to create a physical machine, go to Administration\Infrastructure Management and click on a laboratory. Then, you can click the new icon to add physical machines.

On the popup panel, insert your Physical Machine parameters. Do this step for each machine in your infrastructure. As an example we have the next Machine on our UnaCloud instance:
* Name: ISC401 (should be the hostname of the machine, on windows you can get it executing hostname on command line console)
* IP Address: 157.253.201.141
* MAC Address: AA:BB:CC:DD:EE:FF (used to automatically start physical machines using Wake-On-Lan)
* CPU Cores: 8
* RAM Memory: 16384 MB
* Operating System: Windows 7
* Hypervisor Path: C:\Program Files\VMware\VIX (The path to hypervisor binaries. For VMware products you must put the path that contains the vmrun.exe.)

In the same way you can add more operating systems, users, groups or hypervisors if needed.

# Using Unacloud

Unacloud has three main functional entities: Clusters, Images and Virtual Machines. Virtual Machine Images are the AMI equivalent in the AWS EC2, and each one represents the virtual machine files that will be copied and deployed in the physical machines. A cluster is an image aggrupation, which allows making deployments of different images at once (like needed in a master-slave cluster schema). Finally, a virtual machine is an image already in execution.

##Creating Images

In order to use Unacloud, you must create an image. In the “My Image” page, under the “Functionalities” menu, you can create a new image pressing the add button. There, you can upload a new image by pressing “Upload Image”. You should fill the form with the image data, like in this example:

![alt tag](https://raw.githubusercontent.com/UnaCloud/unacloudIaaS2.0/gh-pages/imageCreation.png)
 
When you click submit, the image files will be sent to Unacloud. This upload may take some time, depending on the files size. You should upload main executable files (.vmx, .vbox) and disk files (.vmdk, .vdi) only. If you mark your image as public, other users can make clusters and images based on it.  
To use a VMware virtual machine, it must have installed the latest version of vmware tools. You can (and must) check that your virtual machine is compatible. To do this use the next commands:

> * vmrun -T ws start //Virtual Machine Path// 
> * vmrun -T ws -gu //user// -gp //password// listProcessesInGuest //Virtual Machine Path// 

Those commands must list all running processes on your virtual machines.
