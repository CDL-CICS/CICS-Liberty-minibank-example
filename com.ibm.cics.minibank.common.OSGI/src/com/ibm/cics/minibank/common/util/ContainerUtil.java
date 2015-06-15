package com.ibm.cics.minibank.common.util;

import com.ibm.cics.server.CCSIDErrorException;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.ChannelErrorException;
import com.ibm.cics.server.CodePageErrorException;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.ContainerErrorException;
import com.ibm.cics.server.InvalidRequestException;

/**
 * Utility class to get container data, and put data into container
 */
public class ContainerUtil {
	public static String getContainerData(Channel channel, String containerName) {
		String data = null;
		Container container;
		try {
			container = channel.getContainer(containerName);
			data = new String(container.get());
			System.out.println("Get data from container. Container="
					+ containerName + "; data=" + data);

		} catch (ContainerErrorException e) {
			System.out.println("getContainerData ContainerErrorException"
					+ ". container=" + containerName);
			e.printStackTrace();
		} catch (ChannelErrorException e) {
			System.out.println("getContainerData ChannelErrorException"
					+ ". container=" + containerName);
			e.printStackTrace();
		} catch (CCSIDErrorException e) {
			System.out.println("getContainerData CCSIDErrorException"
					+ ". container=" + containerName);
			e.printStackTrace();
		} catch (CodePageErrorException e) {
			System.out.println("getContainerData CodePageErrorException"
					+ ". container=" + containerName);
			e.printStackTrace();
		}

		return data;
	}
	
	public static void putContainerData(Channel channel, String containerName,
			String data) {
		try {
			Container container = channel.createContainer(containerName);
			container.put(data.getBytes());
			System.out.println("Put data to container. Container="
					+ containerName + "; data=" + data.getBytes());

		} catch (ContainerErrorException e) {
			System.out.println("putContainerData ContainerErrorException"
					+ ". container=" + containerName + "; data=" + data);
			e.printStackTrace();
		} catch (ChannelErrorException e) {
			System.out.println("putContainerData ChannelErrorException"
					+ ". container=" + containerName + "; data=" + data);
			e.printStackTrace();
		} catch (InvalidRequestException e) {
			System.out.println("putContainerData InvalidRequestException"
					+ ". container=" + containerName + "; data=" + data);
			e.printStackTrace();
		} catch (CCSIDErrorException e) {
			System.out.println("putContainerData CCSIDErrorException"
					+ ". container=" + containerName + "; data=" + data);
			e.printStackTrace();
		} catch (CodePageErrorException e) {
			System.out.println("putContainerData CodePageErrorException"
					+ ". container=" + containerName + "; data=" + data);
			e.printStackTrace();
		}
	}

}
