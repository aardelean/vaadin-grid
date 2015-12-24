package work;

public class RoleMappings {
	private boolean access;
	private boolean writeAccess;

	public RoleMappings(boolean access, boolean writeAccess) {
		this.access = access;
		this.writeAccess = writeAccess;
	}

	public boolean hasAccess() {
		return access;
	}

	public boolean hasWriteAccess() {
		return writeAccess;
	}
}
