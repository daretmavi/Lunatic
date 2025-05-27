![Workflow](https://github.com/Ifiht/Lunatic/actions/workflows/gradle.yml/badge.svg)
![BUILD Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FIfiht%2FLunatic%2Frefs%2Fheads%2Fmain%2Fbadges%2Fbuild.json)
![PAPER Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FIfiht%2FLunatic%2Frefs%2Fheads%2Fmain%2Fbadges%2Ftest-paper.json)
![PURPUR Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FIfiht%2FLunatic%2Frefs%2Fheads%2Fmain%2Fbadges%2Ftest-purpur.json)
![FOLIA Endpoint Badge](https://img.shields.io/endpoint?url=https%3A%2F%2Fraw.githubusercontent.com%2FIfiht%2FLunatic%2Frefs%2Fheads%2Fmain%2Fbadges%2Ftest-folia.json)

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
