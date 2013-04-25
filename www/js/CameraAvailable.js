//
//  CameraAvailable.js
//
//
//  Created by Lukas Klein on 08-19-11.
//  MIT Licensed
//  Copyright (c) Lukas Klein

function CameraAvailable() {};

CameraAvailable.prototype.hasCamera = function(result)
{
return PhoneGap.exec("CameraAvailable.hasCamera", GetFunctionName(result));
}

PhoneGap.addConstructor(function()
{
if(!window.plugins)
{
window.plugins = {};
}
window.plugins.CameraAvailable = new CameraAvailable();
});
