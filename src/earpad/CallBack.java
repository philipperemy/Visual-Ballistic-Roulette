package earpad;

interface CallBack
{
	// True: terminate thread.
	// False: no we are not done yet.
	boolean onResponse(String response);

	void onError(Exception e);

}
