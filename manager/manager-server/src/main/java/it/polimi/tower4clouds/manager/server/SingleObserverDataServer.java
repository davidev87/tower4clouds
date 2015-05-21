/**
 * Copyright (C) 2014 Politecnico di Milano (marco.miglierina@polimi.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.polimi.tower4clouds.manager.server;

import it.polimi.tower4clouds.manager.MonitoringManager;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleObserverDataServer extends ServerResource {

	private static final Logger logger = LoggerFactory
			.getLogger(SingleObserverDataServer.class);

	@Delete
	public void deleteObserver() {
		try {
			MonitoringManager manager = (MonitoringManager) getContext()
					.getAttributes().get("manager");
			String observerId = (String) this.getRequest().getAttributes()
					.get("id");
			String metricName = (String) this.getRequest().getAttributes()
					.get("metricname");

			manager.unregisterObserver(metricName, observerId);
			this.getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

		} catch (Exception e) {
			logger.error("Error while deleting observer", e);
			this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					e.getMessage());
			this.getResponse().setEntity(
					"Error while deleting observer: " + e.getMessage(),
					MediaType.TEXT_PLAIN);
		} finally {
			this.getResponse().commit();
			this.commit();
			this.release();
		}
	}

}
