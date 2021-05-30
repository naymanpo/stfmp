fullname: MAN NAY
class : weekend


Secure Text File Management Protocol - STFMP/1.0

1. Introduction

Secure Text File Management Protocol is an application-level protocol which is designed to provide text file managment services.

2. Architecture

STFMP uses concept of Client/Server model. The server, in default, binds to port 9999.

3. Connection

STFMP is a connection-oriented and stateful protocol. The connection is kept alive until client requested for closing.

4. Message

STFMP message consist of requests from client to server and responses from server to client.  In concern of security, STFMP uses encryption technique to avoid passive attacks from third parties. After the connection was established, the server immediately send an encryption key to the client. Then each party communicate each other using the encrypted message.

5. Request

STFMP request is a one-line message. The message consists three parts: protocol version, action, and optional parameters (PROTOCOL_VERSION##ACTION##[PARAMS]). Each part is separated by ## characters. The line is ended by CRLF.

E.g.:
 - STFMP/1.0##write##My File.txt#Hello world!
 - STFMP/1.0##view##My File.txt
 - STFMP/1.0##close##

 	5.1. Protocol Version

	The protocol version is defined in a format of STFMP/VERSION_NAME. E.g.: STFMP/1.0.

	5.2. Action

	STFMP supports three operations:
	- write: write content to the file
	- view: view content of the file
	- close: close the connection.

	5.3. Parameters

	The prarameter is ignored for close action, but it is required for write and view operations. Eah parameter is separated by # character.

6. Response

SFTMP response is also a one-line message. The message consists of three parts: protocol version, status and data (PROTOCOL_VERSION##STATUS##DATA). Each part is separated by # character. The line is ended by CRLF character.

E.g.:
- SFTMP/1.0##ok##The file has been written.
- SFTMP/1.0##ok##Hello world!
- SFTMP/1.0##not_found##File not found.
- SFTMP/1.0##invalid##Invalid request.

	6.1. Status
	CCP consists of two status:
	- ok
	- not_found
	- invalid.

	6.2. Data

	The data can be either result or message.
	E.g.:
	- The file has been written.
	- Hello world!









