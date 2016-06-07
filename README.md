# woopra-reporting-sdk


To start development using the sdk, you need to define a bean like the following:
  @Bean
	public WoopraFrontdoor getWoopraFrontdoor() {
		WoopraFrontdoor woopraFrontdoor = new WoopraFrontdoor("username",
				"password", "woopra_version");
		return woopraFrontdoor;
	}
	
	where username is the client_id
	password is the client_secret
	woopra_version is the api version that is exposed from Woopra themselves.
	
	You should have the client_id and the client_secret from your woopra account.
