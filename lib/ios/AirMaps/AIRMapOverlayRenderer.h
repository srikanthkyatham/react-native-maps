#import <MapKit/MapKit.h>

@interface AIRMapOverlayRenderer : MKOverlayRenderer

@property (nonatomic, assign) NSInteger rotation;
@property (nonatomic, assign) CGFloat transparency;
@property (nonatomic, assign) CGFloat bearing;
@property (nonatomic, assign) RCTResizeMode resizeMode;

@end
