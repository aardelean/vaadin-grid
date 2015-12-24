package work.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AgeInfo {

	@Column(name = "ALIVE")
	private Boolean alive;
	@Column(name = "AGE")
	private Integer age;
	@Column(name = "BIRTHDATE")
	private double birthDate;

	public Boolean getAlive() {
		return alive;
	}

	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public double getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(double birthDate) {
		this.birthDate = birthDate;
	}
}
