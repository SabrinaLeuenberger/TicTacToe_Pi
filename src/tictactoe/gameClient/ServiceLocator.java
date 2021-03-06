package tictactoe.gameClient;

import java.util.logging.Logger;

import tictactoe.gameClient.clientClasses.TicTacToeClient;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * The singleton instance of this class provide central storage for resources
 * used by the program. It also defines application-global constants, such as
 * the application name.
 * 
 * @author Brad Richards
 */
public class ServiceLocator {
	private static ServiceLocator serviceLocator; // singleton

	// Resources
	private Logger logger;
	private TicTacToeClient connector;

	/**
	 * Factory method for returning the singleton
	 * 
	 * @param mainClass
	 *            The main class of this program
	 * @return The singleton resource locator
	 */
	public static ServiceLocator getServiceLocator() {
		if (serviceLocator == null)
			serviceLocator = new ServiceLocator();
		return serviceLocator;
	}

	/**
	 * Private constructor, because this class is a singleton
	 * 
	 * @param appName
	 *            Name of the main class of this program
	 */
	private ServiceLocator() {
		// Currently nothing to do here. We must define this constructor anyway,
		// because the default constructor is public
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public TicTacToeClient getConnector() {
		return connector;
	}

	public void setConnector(TicTacToeClient connector) {
		this.connector = connector;
	}

}
