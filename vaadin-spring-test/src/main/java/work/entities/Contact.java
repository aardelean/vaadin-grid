package work.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity(name = "contacts")
public class Contact {

	@Id @GeneratedValue
	private Long id;
	@Column(name = "FIRSTNAME")
	private String firstName;
	@Column(name = "LASTNAME")
	private String lastName;

	@Column
	private Integer vendor;

	@Column(name = "VALIDTO")
	@NotNull
	private Date validTo;

	@Column(name = "VALIDFROM")
	@NotNull
	private Date validFrom;

	@Embedded
	private AgeInfo ageInfo;

	public Contact(){}

	public Contact(Long id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Integer getVendor() {
		return vendor;
	}

	public void setVendor(Integer vendor) {
		this.vendor = vendor;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public AgeInfo getAgeInfo() {
		return ageInfo;
	}

	public void setAgeInfo(AgeInfo ageInfo) {
		this.ageInfo = ageInfo;
	}
}
