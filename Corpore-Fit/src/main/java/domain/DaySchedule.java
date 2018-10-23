
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Embeddable
@Access(AccessType.PROPERTY)
public class DaySchedule {

	private DayName	day;
	private String	morningStart;
	private String	morningEnd;
	private String	afternoonStart;
	private String	afternoonEnd;

	@NotNull
	@Enumerated(EnumType.STRING)
	public DayName getDay() {
		return this.day;
	}

	public void setDay(final DayName day) {
		this.day = day;
	}

	@Pattern(regexp = "^\\d{2}:\\d{2}$")
	public String getMorningStart() {
		return this.morningStart;
	}

	public void setMorningStart(final String morningStart) {
		this.morningStart = morningStart;
	}

	@Pattern(regexp = "^\\d{2}:\\d{2}$")
	public String getMorningEnd() {
		return this.morningEnd;
	}

	public void setMorningEnd(final String morningEnd) {
		this.morningEnd = morningEnd;
	}

	@Pattern(regexp = "^\\d{2}:\\d{2}$")
	public String getAfternoonStart() {
		return this.afternoonStart;
	}

	public void setAfternoonStart(final String afternoonStart) {
		this.afternoonStart = afternoonStart;
	}

	@Pattern(regexp = "^\\d{2}:\\d{2}$")
	public String getAfternoonEnd() {
		return this.afternoonEnd;
	}

	public void setAfternoonEnd(final String afternoonEnd) {
		this.afternoonEnd = afternoonEnd;
	}

}
