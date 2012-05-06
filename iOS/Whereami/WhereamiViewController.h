//
//  WhereamiViewController.h
//  Whereami
//
//  Created by joeconway on 7/31/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface WhereamiViewController : UIViewController
    <CLLocationManagerDelegate, MKMapViewDelegate, UITextFieldDelegate>
{
    CLLocationManager *locationManager;
    IBOutlet MKMapView *worldView;
    IBOutlet UIActivityIndicatorView *activityIndicator;
    IBOutlet UITextField *locationTitleField;
    NSURLConnection *connection;
}
- (void)findLocation;
- (void)foundLocation:(CLLocation *)loc;
- (IBAction)sendLocationToServer:(id)sender;
@end