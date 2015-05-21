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
import it.polimi.tower4clouds.model.ontology.Resource;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class SingleResourceDataServer extends ServerResource {

	private Logger logger = LoggerFactory
			.getLogger(SingleResourceDataServer.class.getName());

	@Delete
	public void deleteResource() {
		String id = (String) this.getRequest().getAttributes().get("id");
		try {
			MonitoringManager manager = (MonitoringManager) getContext()
					.getAttributes().get("manager");

			manager.deleteResource(id);

			this.getResponse().setStatus(Status.SUCCESS_NO_CONTENT);

		} catch (NotFoundException e) {
			logger.error("Resource {} does not exist", id);
			this.getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					"Resource " + id + " does not exist");
			this.getResponse().setEntity("Resource " + id + " does not exist",
					MediaType.TEXT_PLAIN);

		} catch (Exception e) {
			logger.error("Error while deleting the component", e);
			this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					e.getMessage());
			this.getResponse().setEntity(
					"Error while deleting the component: " + e.getMessage(),
					MediaType.TEXT_PLAIN);
		} finally {
			this.getResponse().commit();
			this.commit();
			this.release();
		}
	}

	@Get
	public void getResource() {
		String id = (String) this.getRequest().getAttributes().get("id");
		try {
			Gson gson = new Gson();
			MonitoringManager manager = (MonitoringManager) getContext()
					.getAttributes().get("manager");

			Resource resource = manager.getResource(id);

			this.getResponse().setStatus(Status.SUCCESS_OK,
					"Resource successfully retrieved");
			this.getResponse().setEntity(gson.toJson(resource),
					MediaType.APPLICATION_JSON);
		} catch (NotFoundException e) {
			logger.error("Resource {} does not exist", id);
			this.getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND,
					"Resource " + id + " does not exist");
			this.getResponse().setEntity("Resource " + id + " does not exist",
					MediaType.TEXT_PLAIN);
		} catch (Exception e) {
			logger.error("Error while getting the resource", e);
			this.getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
					e.getMessage());
			this.getResponse().setEntity(
					"Error while getting the resource: " + e.getMessage(),
					MediaType.TEXT_PLAIN);
		} finally {
			this.getResponse().commit();
			this.commit();
			this.release();
		}
	}

}