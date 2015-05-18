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
import it.polimi.tower4clouds.manager.NotFoundException;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleRuleDataServer extends ServerResource {

	private Logger logger = LoggerFactory.getLogger(SingleRuleDataServer.class
			.getName());

	@Delete
	public void uninstallMonitoringRule(Representation rep) {
		try {
			MonitoringManager manager = (MonitoringManager) getContext()
					.getAttributes().get("manager");
			String id = (String) this.getRequest().getAttributes().get("id");
			manager.uninstallRule(id);
			this.getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
		} catch (NotFoundException e) {
			String message = "Rule does not exist: " + e.getMessage();
			logger.error(message);
			this.getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					e.getMessage());
			this.getResponse().setEntity(message, MediaType.TEXT_PLAIN);
		} catch (Exception e) {
			logger.error("Error while uninstalling the rule", e);
			this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					e.getMessage());
			this.getResponse().setEntity(
					"Error while uninstalling the rule: " + e.getMessage(),
					MediaType.TEXT_PLAIN);
		} finally {
			this.getResponse().commit();
			this.commit();
			this.release();
		}
	}

}
