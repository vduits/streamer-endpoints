# Streamer Endpoints
This project contains a multitude of endpoints with the intention to be used by Twitch bots.  
It has currently been targeted to work for a single streamer, but the FollowAge command is universally usable.

### Currently available endpoints
Endpoints under /api/v1:
* [Quotes](#Quotes)
* [FollowAge](#FollowAge)

#### Quotes
* `GET /quote`
Returns all available quotes
* `GET /quote/{quoteId}/quote`
Returns textual contents of a single quote using numerical id.
* `GET /quote/random`
Returns a random quote.
* `POST /quote/add`
Allows you to add a quote with a string as body. Responds with a new quote-id.

#### FollowAge
* `GET /followage/{firstUser}/following/{secondUser}`
By supplying twitch usernames this twitchEndpoint will calculate the amount of time 
the first user that is supplied has been following the second user.

### Development
This section contains everything to run the application.

#### Requirements
* Java 11
* MariaDB or MySQL database
* Twitch application
* (Optional) Docker

#### Getting Started
An `application.yaml.example` has been provided to create your own `application.yaml`.  
Most notably, under twitch, `client-id` and `client-secret` has to be set by [creating an application at Twitch](https://dev.twitch.tv/console/apps).  


#### Docker
A docker-compose.yml has been provided at the root of the repository.
This allows you to quickly spin up a database that is used to store the quotes.
Refer to the [documentation](https://docs.docker.com/compose/) how to use docker compose.
