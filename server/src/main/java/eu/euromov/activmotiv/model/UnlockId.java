package eu.euromov.activmotiv.model;

import java.time.Instant;
import java.util.Objects;

public class UnlockId {
	private Instant time;
	private Participant participant;
	
	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Override
	public int hashCode() {
		return Objects.hash(participant, time);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UnlockId other = (UnlockId) obj;
		return Objects.equals(participant, other.participant) && Objects.equals(time, other.time);
	}
}
