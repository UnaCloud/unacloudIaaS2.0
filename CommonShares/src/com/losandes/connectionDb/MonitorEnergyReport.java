package com.losandes.connectionDb;

import java.util.Date;

public class MonitorEnergyReport {
	
	
	private String time,RDTSC,elapsedTime,CPUFrequency,processorPower,
	cumulativeProcessorEnergyJoules,cumulativeProcessorEnergyMhz,
	IAPower,cumulativeIAEnergy,cumulativeIA,packageTemperature,
	packageHot,packagePowerLimit, hostName;	
	private Date registerDate;
	
	public MonitorEnergyReport(){
		
	}
	
	public MonitorEnergyReport(String line){
		String[] data = line.split(",");
		time = data[0];
		RDTSC = data[1];
		elapsedTime = data[2];
		CPUFrequency = data[3];
		processorPower = data[4];
		cumulativeProcessorEnergyJoules = data[5];
		cumulativeProcessorEnergyMhz = data[6];
		IAPower = data[7];
		cumulativeIAEnergy = data[8];
		cumulativeIA = data[9];
		packageTemperature = data[10];
		packageHot = data[11];
		packagePowerLimit = data[12];
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String ti) {
		time = ti;
	}
	public String getRDTSC() {
		return RDTSC;
	}
	public void setRDTSC(String rDTSC) {
		RDTSC = rDTSC;
	}
	public String getElapsedTime() {
		return elapsedTime;
	}
	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	public String getCPUFrequency() {
		return CPUFrequency;
	}
	public void setCPUFrequency(String cPUFrequency) {
		CPUFrequency = cPUFrequency;
	}
	public String getProcessorPower() {
		return processorPower;
	}
	public void setProcessorPower(String processorPower) {
		this.processorPower = processorPower;
	}
	public String getCumulativeProcessorEnergyJoules() {
		return cumulativeProcessorEnergyJoules;
	}
	public void setCumulativeProcessorEnergyJoules(
			String cumulativeProcessorEnergyJoules) {
		this.cumulativeProcessorEnergyJoules = cumulativeProcessorEnergyJoules;
	}
	public String getCumulativeProcessorEnergyMhz() {
		return cumulativeProcessorEnergyMhz;
	}
	public void setCumulativeProcessorEnergyMhz(String cumulativeProcessorEnergyMhz) {
		this.cumulativeProcessorEnergyMhz = cumulativeProcessorEnergyMhz;
	}
	public String getIAPower() {
		return IAPower;
	}
	public void setIAPower(String iAPower) {
		IAPower = iAPower;
	}
	public String getCumulativeIAEnergy() {
		return cumulativeIAEnergy;
	}
	public void setCumulativeIAEnergy(String cumulativeIAEnergy) {
		this.cumulativeIAEnergy = cumulativeIAEnergy;
	}
	public String getCumulativeIA() {
		return cumulativeIA;
	}
	public void setCumulativeIA(String cumulativeIA) {
		this.cumulativeIA = cumulativeIA;
	}
	public String getPackageTemperature() {
		return packageTemperature;
	}
	public void setPackageTemperature(String packageTemperature) {
		this.packageTemperature = packageTemperature;
	}
	public String getPackageHot() {
		return packageHot;
	}
	public void setPackageHot(String packageHot) {
		this.packageHot = packageHot;
	}
	public String getPackagePowerLimit() {
		return packagePowerLimit;
	}
	public void setPackagePowerLimit(String packagePowerLimit) {
		this.packagePowerLimit = packagePowerLimit;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
}
