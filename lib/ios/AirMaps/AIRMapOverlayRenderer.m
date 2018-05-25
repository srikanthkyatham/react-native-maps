#import "AIRMapOverlayRenderer.h"
#import "AIRMapOverlay.h"

@implementation AIRMapOverlayRenderer

- (void)drawMapRect:(MKMapRect)mapRect zoomScale:(MKZoomScale)zoomScale inContext:(CGContextRef)context {
    UIImage *image = [(AIRMapOverlay *)self.overlay overlayImage];
    
    CGContextSaveGState(context);
    
    CGImageRef imageReference = image.CGImage;
    
    MKMapRect theMapRect = [self.overlay boundingMapRect];
    CGRect theRect = [self rectForMapRect:theMapRect];
    
    if (_bearing != 0) {
        CGContextRotateCTM(context, [self degreesToRadians:_bearing]);
    } else {
        CGContextRotateCTM(context, M_PI);
    }
    CGContextScaleCTM(context, -1.0, 1.0);
    CGContextAddRect(context, theRect);
    CGContextTranslateCTM(context, 0.0, -theRect.size.height);
    CGContextDrawImage(context, theRect, imageReference);
    
    CGContextRestoreGState(context);
}

- (CGFloat)degreesToRadians:(CGFloat)degrees {
    // kind of hacky but for some reason overlays and rendered upside down
    // this seems to fix the issue
    degrees += 180;
    if (degrees > 360) {
        degrees -= 360;
    }
    return (M_PI * degrees / 180.0);
}

- (BOOL)canDrawMapRect:(MKMapRect)mapRect zoomScale:(MKZoomScale)zoomScale {
    return [(AIRMapOverlay *)self.overlay overlayImage] != nil;
}

@end

