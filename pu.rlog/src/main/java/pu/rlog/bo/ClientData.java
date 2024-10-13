package pu.rlog.bo;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientData
{
private final int clientId;
private final SocketNode SocketNode;
private String ipAddress;
private String hostName;
private final Date timeStarted;
private Date timeLastActivity;
private int historySize;
private String user;
// @@NOG Moet dit niet veel meer zijn=
}
