package it.eng.unipa.filesharing.resource;

import java.util.Objects;

public class BucketType {

	private final String name;
	private final String description;
	private final ResourceRepository repository;
	
	public BucketType(String name, String description, ResourceRepository repository) {
		super();
		this.name = name;
		this.description = description;
		this.repository = repository;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ResourceRepository getRepository() {
		return repository;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BucketType)) return false;
		BucketType that = (BucketType) o;
		return Objects.equals(getName(), that.getName()) &&
				Objects.equals(getDescription(), that.getDescription()) &&
				Objects.equals(getRepository(), that.getRepository());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getDescription(), getRepository());
	}
}


