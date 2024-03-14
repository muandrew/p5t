//
//  ContentView.swift
//  QRTransferMac
//
//  Created by Andrew Mu on 12/20/23.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello, world!")
            QRGeneratorView()
        }
        .padding()
        
    }
}

#Preview {
    ContentView()
}

struct QRGeneratorView: View {
    @State private var text = ""
    
    var body: some View {
        VStack {
            TextField("Enter code", text: $text)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            var comp = URLComponents()
            let _ = comp.scheme = "https"
            let _ = comp.host = "p5t.me"
            let _ = comp.queryItems = [URLQueryItem(name: "q", value: text),]
            var url = comp.url?.absoluteString
            var finalUrl = url != nil ? url! : "blah"
            
            Text(finalUrl)
            
            Image(nsImage: getQRCodeData(text: finalUrl)!)
                .resizable()
                .frame(width: 200, height: 200)
        }
    }
    
    func getQRCodeData(text: String) -> NSImage? {
        guard let filter = CIFilter(name: "CIQRCodeGenerator") else { return nil }
        let data = text.data(using: .ascii, allowLossyConversion: false)
        filter.setValue(data, forKey: "inputMessage")
        guard let ciimage = filter.outputImage else { return nil }
        let transform = CGAffineTransform(scaleX: 10, y: 10)
        let scaledCIImage = ciimage.transformed(by: transform)
        let uiimage = ciToNS(input: scaledCIImage)
        return uiimage
    }
    
    func ciToNS(input: CIImage) -> NSImage {
        let rep = NSCIImageRep(ciImage: input)
        let nsImage = NSImage(size: rep.size)
        nsImage.addRepresentation(rep)
        return nsImage
    }
}
