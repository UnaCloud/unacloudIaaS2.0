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

#UnaCloud server configuration guide
## Creating Physical Laboratories
UnaCloud sorts Physical machines by Laboratories for GUI convenience. By default, a Laboratory is created in the database to add physical machines to it. If you need to change or create more laboratories you must do it directly in the database. A physical machine is a desktop or dedicated machine that has UnaCloud agent installed and is used to deploy opportunistic virtual machines. Each physical machine is associated to a Laboratory. In order to create a physical machine, go to Administration\Infrastructure Management and click on a laboratory. Then, you can click the new icon to add physical machines.
On the popup panel, insert your Physical Machine parameters. Do this step for each machine in your infrastructure. As an example we have the next Machine on our UnaCloud instance:
* Name: ISC401 (should be the hostname of the machine, on windows you can get it executing hostname on command line console)
* IP Address: 157.253.201.141
* MAC Address: AA:BB:CC:DD:EE:FF (used to automatically start physical machines using Wake-On-Lan)
* CPU Cores: 8
* RAM Memory: 16384 MB
* Hard Disk Space: 50 GB (for UnaCloud)
* Architecture: x86
* Hypervisor Path: C:\Program Files\VMware\VIX (The path to hypervisor binaries. For VMware products you must put the path that contains the vmrun.exe.)
* Operating System: Windows 7
In the same way you can add more operating systems, users, groups or hypervisors if needed.

# Using Unacloud

Unacloud has three main functional entities: Clusters, Images and Virtual Machines. Virtual Machine Images are the AMI equivalent in the AWS EC2, and each one represents the virtual machine files that will be copied and deployed in the physical machines. A cluster is an image aggrupation, which allows making deployments of different images at once (like needed in a master-slave cluster schema). Finally, a virtual machine is an image already in execution.

##Creating Images

In order to use Unacloud, you must create an image. In the “My Image” page, under the “Functionalities” menu, you can create a new image pressing the add button. There, you can upload a new image by pressing “Upload Image”. You should fill the form with the image data, like in this example:


 
When you click submit, the image files will be sent to Unacloud. This upload may take some time, depending on the files size. You should upload main executable files (.vmx, .vbox) and disk files (.vmdk, .vdi) only. If you mark your image as public, other users can make clusters and images based on it.  
To use a VMware virtual machine, it must have installed the latest version of vmware tools. You can (and must) check that your virtual machine is compatible. To do this use the next commands:

> * vmrun -T ws start //Virtual Machine Path// 
> * vmrun -T ws -gu //user// -gp //password// listProcessesInGuest //Virtual Machine Path// 

Those commands must list all running processes on your virtual machines.
