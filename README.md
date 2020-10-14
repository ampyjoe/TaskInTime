# TaskInTime
## Instructional Aims
The idea for this sample is to show how to manage an SMS conversation between a user and a system. In this context a "conversation" represents a set of any number of interactions between the user and the system, where an interaction comprises a command from the user, followed by a reply from the system. During each interaction the system may also take some action to read/write data to a data store.

The instructional value of this sample is in seeing how to structure code to manage this conversation, and the sample does this by using the state pattern as described in the GoF book.

The techniques used to persist the data used in the sample are not chosen for their instructional value; they are chosen for the their convenience. Therefore, the data is stored in plain text files. Anyone trying the sample can see the use of the state pattern in managing the SMS conversation by cloning the repository, running the project, and then running a Test class which simulates the requests that would come from Twilio if multiple users were sending texts to the phone number set up to forward to the webhook (`SMSWebhookHandler`).

On seeing it working with the many different test runs supplied, the user can set up a Twilio account and try out the system by sending SMS from a cellphone and/or web app such as google voice. But the idea is to make it easy to see the code working and play with it.

## Code Included

### Packages that illustrate coding techniques

These packages are complete and working though may benefit from some tidy up.

`com.example.handler`

Contains the webhook, `SMSWebhookHandler` (plus a very simple utility), and the class that models the state of the conversation, `JobContext`.

`com.example.handler.process`

Contains an interface, `ProcessInput`, and a number of concrete classes to model different parts of the conversation.

### Packages that are designed to simplify trying out the sample
These packages are working, but do need some tidy-up.

`com.example.data`

Contains files related to manipulating the data. 
This includes the classes `User` to model users (the people who text the system), and `Job` to model jobs (jobs currently waiting to be completed).
It also includes a class `Util`, that handles all of the persistence needed.
`Util` is currently set up to use plain text files for both users and jobs, with a line for each entity (user or job) and with each field separated by a caret. A version of `Util` could be created that uses, say, a database like `derby`, but plain text files are conveniently simple for set up and modification.

`com.example.test`

Contains a class, `Test`, that simulates the Twilio requests requests to the webhook, `SMSWebhookHandler`. Test reads a plain text file to determine the sequence of requests it should make and also includes a cookie, just as a Twilio request would. The cookie is only stored in memory and thus re-running `Test` simulates what happens when the session has expired.

## Further Notes
Possible additions to the sample.
- Some more states. E.g. another command leading to a number of new states for adding data
- A Test application that uses Twilio to send SMS using special accounts
- An IVR using the same techniques

Drawbacks to current implementation
- Files will grow large if more and more jobs added (could archive completed jobs to another file or use a db)
