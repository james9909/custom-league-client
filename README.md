# custom-league-client

You can support me and this project by buying a coffee

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/hawolt)

## Dependencies

- [riot-xmpp](https://github.com/hawolt/riot-xmpp)
- [league-of-legends-rtmp](https://github.com/hawolt/league-of-legends-rtmp)
- [league-client-api](https://github.com/hawolt/league-client-api)
- [league-of-legends-rms](https://github.com/hawolt/league-of-legends-rms)

## How to setup the project using IntelliJ

1. within Intellij select `File` -> `New` -> `Project from Version Control...`
2. insert `git@github.com:hawolt/custom-league-client.git` for the `URL` field and hit `Clone`
3. IntelliJ should automatically detect the Maven framework, if this is not the case you can rightclick the custom-league-client folder in the Project hierarchy and select `Add Framework Support...` then select `Maven`
4. Make sure you are actually using a compatible Java version by selecting `File` -> `Project Structure`, navigate to `Project` within `Project Settings` and make sure both `SDK` and `Language level` have Java Version 17 or higher selected, hit `OK`
5. To run the Code navigate to `SwingUI/src/main/java/com/hawolt` and rightclick `LeagueClientUI`, select `Run LeagueClientUI.main()`
