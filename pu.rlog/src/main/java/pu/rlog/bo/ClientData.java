package pu.rlog.bo;

import java.util.Date;

import lombok.Data;

@Data
public class ClientData
{
	private final Integer clientId;
	private final String ipAddress;
	private final Date timeStarted;
	private final Date timeLastActivity;
	private final int historySize;
	private final String user;
	// @@NOG Moet dit niet veel meer zijn=
}
