//
//  orgViewController.h
//  GenDev
//
//  Created by Sergio Antonio Pino Gallardo on 5/6/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import <MapKit/MapKit.h>

@interface orgViewController : UIViewController
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
