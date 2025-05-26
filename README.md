# ![JUnit Tests](https://github.com/Ifiht/Lunatic/actions/workflows/gradle.yml/badge.svg)

# Lunatic - a Moon plugin for Paper/Folia**
The Moon shines bright on this world :rice_scene:  

Get the details on [Modrinth](https://modrinth.com/plugin/lunamatic)

## Development
> [!IMPORTANT]
> After significant effort spent on this project, there are two rules I follow that I insist any contributors also use when adding new code, mainly that **ALL** code must run in one of two scopes:
1. **GlobalRegion**: code that runs on a per-world basis, in the Paper/Folia global region scheduler.  
   _Examples: moon phase, random tick speed, weather_
2. **PlayerSpecific**: code that runs specific to the triggering player, contextualized to their world and region.  
   _Examples: player messages, commands, entity spawning_

If the code doesn't fit either of those two scopes, it probably isn't a good fit for this plugin.