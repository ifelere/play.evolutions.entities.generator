# play.evolutions.entities.generator
Scala Slick case classes and table mapping generator

Slick's SourceCodeGenerator does an amazing job of generating supporting classes straight from the database. I am in a virgin stage of use slick for playframework and I want
to see source files for table mappings. Plus I want to be able to support custom types for a project I am working on.
In the past I got by with just a few table mappings that I wrote so it wasn't tedious. This is not the case for a project I am working on now and it was tedious to write 
the play evolution scripts and write table mappings manually. Therefore I turned to Slick's SourceCodeGenerator but for the reasons I meansioned before it didn't provide the customization level I wanted (at least from my current impresssion) and it didn't generate source files I could tweak later. This is the reason why I created this app. I figured I am not the only one that faced this situation and could use this tool. 

It is a JavaFX desktop application.

It uses the evolution scripts you created for your application (playframework, usually) to create case classes for tables. It can also generate a trait to encapsulate table mappings for the case classes as well as TableQuery variables. For now, everything is premitty much scala code generation. I anticipated that the architecture may be useful to support other languages. I don't need those currently so I didn't give that feature much attention. Nevertheless, it will be very interesting to see contributions that improves this project or add support for more languages. 
