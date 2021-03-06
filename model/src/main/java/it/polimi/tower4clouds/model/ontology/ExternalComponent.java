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
package it.polimi.tower4clouds.model.ontology;

public class ExternalComponent extends Component {

	public ExternalComponent(String type, String id) {
		super(type,id);
	}
	
	public ExternalComponent() {
	}
	
	
	@Override
	public String toString() {
		return "ExternalComponent [cloudProvider=" + cloudProvider
				+ ", location=" + location + ", clazz=" + getClazz()
				+ ", type=" + getType() + ", id=" + getId() + "]";
	}

	private String cloudProvider;
	private String location;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCloudProvider() {
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((cloudProvider == null) ? 0 : cloudProvider.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExternalComponent other = (ExternalComponent) obj;
		if (cloudProvider == null) {
			if (other.cloudProvider != null)
				return false;
		} else if (!cloudProvider.equals(other.cloudProvider))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

}
