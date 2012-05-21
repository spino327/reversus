//
//  orgViewController.m
//  GenDev
//
//  Created by Sergio Antonio Pino Gallardo on 5/6/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "orgViewController.h"
#import "MapPoint.h"

#import <Foundation/Foundation.h>
#import <CoreFoundation/CoreFoundation.h>
#include <sys/socket.h>
#include <netinet/in.h>
#if TARGET_OS_IPHONE
#import <CFNetwork/CFNetwork.h>
#endif

#define PORT 8080


NSString * const MapTypePrefKey = @"MapTypePrefKey";

@interface orgViewController ()

@end

@implementation orgViewController

- (void)viewDidLoad
{
    NSLog(@"viewDidLoad");
    [worldView setShowsUserLocation:YES];
    MKUserLocation* userLoc = [worldView userLocation];
    //lat = [NSString stringWithFormat:@"%f", [[userLoc location] coordinate].latitude];
    //lon = [NSString stringWithFormat:@"%f", [[userLoc location] coordinate].longitude];
    
    /*lat = [[NSNumber numberWithDouble:[[userLoc location] coordinate].latitude] stringValue];
    lon = [[NSNumber numberWithDouble:[[userLoc location] coordinate].longitude] stringValue];
    
    
    NSLog(lat);
    NSLog(lon);*/
}

- (void)viewDidUnload
{
    NSLog(@"viewDidUnload");
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    NSLog(@"shouldAutorotateToInterfaceOrientation");
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
}

+ (void)initialize
{
    NSLog(@"initialize");
    NSDictionary *defaults = [NSDictionary 
                              dictionaryWithObject:[NSNumber numberWithInt:1]
                              forKey:MapTypePrefKey];
    [[NSUserDefaults standardUserDefaults] registerDefaults:defaults];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    NSLog(@"initWithNibName");
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if(self) {
        locationManager = [[CLLocationManager alloc] init];        
        [locationManager setDelegate:self];        
        [locationManager setDesiredAccuracy:kCLLocationAccuracyBest];
    }
    return self;
}

- (BOOL)textFieldShouldReturn:(UITextField *)tf
{
    NSLog(@"textFieldShouldReturn");
    // This method isn't implemented yet - but will be soon.
    [self findLocation];
    
    [tf resignFirstResponder];
    
    return YES;
}
- (void)findLocation
{
    NSLog(@"findLocation");
    [locationManager startUpdatingLocation];
    [activityIndicator startAnimating];
}

- (void)foundLocation:(CLLocation *)loc
{
    NSLog(@"foundLocation");
    CLLocationCoordinate2D c = [loc coordinate];
    //lat = [NSString stringWithFormat:@"%f", c.latitude];
    //lon = [NSString stringWithFormat:@"%f", c.longitude];
    
    // Create an instance of MapPoint with the current data
    MapPoint *mp = [[MapPoint alloc] initWithCoordinate:c
                                                  title:nil];
    // Add it to the map view 
    [worldView addAnnotation:mp];
    
    // Zoom the region to this location
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(c, 50, 50);
    [worldView setRegion:region animated:YES];
    
    [activityIndicator stopAnimating];
    [locationManager stopUpdatingLocation];
}

- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation
{
    NSLog(@"locationManager");
    // How many seconds ago was this new location created?
    NSTimeInterval t = [[newLocation timestamp] timeIntervalSinceNow];
    
    // CLLocationManagers will return the last found location of the 
    // device first, you don't want that data in this case.
    // If this location was made more than 3 minutes ago, ignore it.
    if (t < -180) {
        // This is cached data, you don't want it, keep looking
        return;
    }
    
    [self foundLocation:newLocation];
}

- (void)locationManager:(CLLocationManager *)manager
       didFailWithError:(NSError *)error
{
    NSLog(@"Could not find location: %@", error);
}

- (void)mapView:(MKMapView *)mv didUpdateUserLocation:(MKUserLocation *)u
{
    NSLog(@"mapView");
    CLLocationCoordinate2D loc = [u coordinate];
    MKCoordinateRegion region = MKCoordinateRegionMakeWithDistance(loc, 50, 50);
    [worldView setRegion:region animated:YES];
}

-(IBAction)sendRequestURL:(id)sender{
    
    NSURLConnection *connInProgress;
    
    NSString *serverURL = [server text];
    NSString *userID = [user text];
    
    NSLog(serverURL);
    NSLog(userID);
    
    NSString* url = [NSString stringWithFormat:
     @"http://%@:8080/gateway?lat=39.6805651774051&lon=-75.7530490200713&user=%@&ts=",serverURL, userID];
    
    /*NSString* url = [NSString stringWithFormat:
                     @"http://%@:8080/gateway?lat=%@&lon=%@&user=%@&ts=",serverURL, lat, lon, userID];*/
    
    
    NSLog(url);
    /*NSMutableURLRequest *request = [NSMutableURLRequest 
     requestWithURL:[NSURL URLWithString:@"http://10.0.1.140:8080/gateway?lat=10&lon=10"] 
     cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData
     timeoutInterval:10];*/
    
    NSMutableURLRequest *request = [NSMutableURLRequest 
                                    requestWithURL:[NSURL URLWithString:url] 
                                    cachePolicy:NSURLRequestReloadIgnoringLocalAndRemoteCacheData
                                    timeoutInterval:10];
    
    [request setHTTPMethod: @"GET"];
    
    
    connInProgress = [[NSURLConnection alloc] initWithRequest: request
                                                     delegate:self
                                             startImmediately:YES];
    
    if(connInProgress!=nil)
        NSLog(@"Failure to create URL connection.");
    
}


- (void)connection:(NSURLConnection *)connectiondidFailWithError:(NSError *)error{
    
    NSLog(@"error info: %@\n", [error localizedDescription]);
}

- (IBAction)backgroundTouch:(id)sender {
    [server resignFirstResponder];
    [user resignFirstResponder];
}


- (void)dealloc
{
    // Tell the location manager to stop sending us messages
    [locationManager setDelegate:nil];
    
}

@end
