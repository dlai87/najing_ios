//
//  BasicVC.m
//  Shuiguang
//
//  Created by dehualai on 3/4/17.
//  Copyright © 2017 NanoMed. All rights reserved.
//

#import "BasicVC.h"
#import "AppDelegate.h"


@interface BasicVC ()

@end

@implementation BasicVC


- (NSManagedObjectContext*) context
{
    if (!_context)
    {
        AppDelegate *app = (AppDelegate*) [[UIApplication sharedApplication] delegate];
        _context = app.managedObjectContext;
    }
    return _context;
}


- (void)viewDidLoad {
    [super viewDidLoad];
    
    // dismiss keyboard when tap outside
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]
                                   initWithTarget:self
                                   action:@selector(dismissKeyboard)];
    [self.view addGestureRecognizer:tap];
}

-(void)viewDidLayoutSubviews{
    [super viewDidLayoutSubviews];
    [self updateSubview];
    [self multiLanguage];
}


-(void)multiLanguage{

}

-(void)updateSubview{

}

-(void)dismissKeyboard{

}

-(void)goBack{
    if(self.navigationController){
        [self.navigationController popViewControllerAnimated:NO];
    }else{
        [self.presentingViewController dismissViewControllerAnimated:NO completion:nil];
    }
}



@end
