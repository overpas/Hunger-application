package by.overpass.hungerprovision;

public enum ConfirmationState {
	PENDING, CONFIRMED, CANCELLED;
	
	public static ConfirmationState currentState = ConfirmationState.PENDING;
}
