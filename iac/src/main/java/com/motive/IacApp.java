package com.motive;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;


public class IacApp {
    public static void main(final String[] args) {
        App app = new App();

        StackProps stackProps = StackProps
                .builder()
                .env(createEnvironment())
                .build();

        PrototypeServiceStack serviceStack = new PrototypeServiceStack(app, "PrototypeService", stackProps);

        app.synth();
    }

    private static Environment createEnvironment() {
        return Environment
                .builder()
                .account("842924693656")
                .region("us-west-2")
                .build();
    }
}

