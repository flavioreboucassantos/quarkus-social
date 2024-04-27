package com.br.flavioreboucassantos;

import io.vertx.core.cli.annotations.Summary;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.Components;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "User Resource", description = "One User: Create One, List All, Edit One, Delete One."),
                @Tag(name = "Relationship Follower Resource", description = "One Relationship Follower: Follow One, List Followers, Unfollow One."),
                @Tag(name = "Post Resource", description = "One Post: Create One, List All from Followed by Follower.")
        },
        info = @Info(
                title = "API Quarkus Social",
                version = "1.0",
                contact = @Contact(
                        name = "Flávio Rebouças Santos",
                        url = "https://github.com/flavioreboucassantos",
                        email = "flavioreboucassantos@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class QuarkusSocialApplication extends Application {
}
