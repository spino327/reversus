<?php
/*
 * Based on code from Google API project
 */

require_once 'lib/apiClient.php';
require_once 'lib/contrib/apiLatitudeService.php';

class Latitude extends CI_Controller {

	public function index()
	{
		session_start();
		$client = new apiClient();
		// Visit https://code.google.com/apis/console to generate your
		// oauth2_client_id, oauth2_client_secret, and to register your oauth2_redirect_uri.
		$client->setClientId('39956969404.apps.googleusercontent.com');
		$client->setClientSecret('PJvonUl1bWXUkrmHh_icSmxw');
		$client->setRedirectUri('http://localhost:8888/server/index.php/latitude');
		$client->setApplicationName("Latitude_Example_App");

		$service = new apiLatitudeService($client);
		
		if (isset($_REQUEST['logout'])) {
		  unset($_SESSION['access_token']);
		}
		
		if (isset($_GET['code'])) {
		  $client->authenticate();
		  $_SESSION['access_token'] = $client->getAccessToken();
		  $redirect = 'http://' . $_SERVER['HTTP_HOST'] . $_SERVER['PHP_SELF'];
		  header('Location: ' . filter_var($redirect, FILTER_SANITIZE_URL));
		}
		
		if (isset($_SESSION['access_token']) && $_SESSION['access_token']) {
		  $client->setAccessToken($_SESSION['access_token']);
		} else {
		  $authUrl = $client->createAuthUrl();
		  $data['authUrl'] = $authUrl;
		}
		
		if ($client->getAccessToken()) {
		  // Start to make API requests.
		  //$location = $service->location->listLocation();
		  //$data['location'] = $location;
		  
		  $currentLocation = $service->currentLocation->get();
		  $data['currentLocation'] = $currentLocation;
		  $_SESSION['access_token'] = $client->getAccessToken();
		}
		
		$this->load->view('templates/latitude', $data);
		
	}
}

?>