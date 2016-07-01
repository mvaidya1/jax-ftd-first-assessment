import vorpal from 'vorpal'
import net from 'net'
import fs from 'fs'
import { hash, compare } from './hashing'

const cli = vorpal()
let server
const host = 'localhost'
const port = 667
const users = {}
let Userlogged = false
/*
*Connects With the Client with the Server
*/
/* Server functions to connect, close, and write to our server */
const ServerConnection = () => {
  server = net.createConnection(port, host, () => {
    return 0
  })
}

const DisconnectServer = (server) => {
  server.end()
}
const writeTo = (string) => server.write(string + '\n')

const writeJSONFile = (object) => {
  server.write(JSON.stringify({ 'files': object }) + '\n')
}
const createFile = (filepath, buffer, username) => {
  return {
    'filePath': filepath,
    'buffer': buffer,
    'username': username
  }
}
/*
register <username> <password>
    * registers a new user with the application
    * the plaintext password willn't be stored anywhere, ever
    * the password will be hashed for storage according to the bcrypt algorithm
*/
const register = cli.command('register <username> <password>')
register
.description('Registers a user with this application')
.action(function (args, callback) {
  ServerConnection()
  return (
    Promise.resolve(users[args.username] !== undefined)
    .then(
      (alreadyRegistered) =>
       alreadyRegistered
       ? this.log('Username already Registered ! Choose another name')
       : hash(args.password)
              .then((hashedPassword) => users[args.username] = hashedPassword)
              .then(() => this.log('Registration successful!'))
      )
      .catch((err) => this.log(`An error occurred: ${err}`))
  )
})
DisconnectServer()
/*
login <username> <password>
    * logs a user in to the command line interface
    * the plaintext password willn't be stored anywhere, ever
    * the commandline application should store the user details necessary for logged-in functionality
    * the hashed password will be compared on the client side
*/
const login = cli.command('login <username> <password>')
login
  .description('Login with a specified username and password')
  .init(function (args, callabck) {
    server = net.createConnection()
  })
  .action(function (args, cb) {
    ServerConnection()
    return (
      Promise.resolve(users[args.username])
        .then(
          (hashedPassword) =>
            hashedPassword === undefined
              ? this.log('Username or password incorrect. Try again!')
              : compare(args.password, hashedPassword)
                .then((correctPassword) => {
                  if (correctPassword) {
                    Userlogged = args.username
                    ? this.log('Successfully logged in!')
                    : this.log('Username or password incorrect. Try again!')
                  }
                })
        )
        .catch((err) => this.log(`An error occurred: ${err}`))
    )
  })
DisconnectServer()

cli
  .command('logout')
  .description('Logs you out, if you are logged in')
  .action((callback) => {
    if (!Userlogged) {
      cli.log('You are not logged in. You have to be logged in to log out.')
    } else {
      Userlogged = false
    }
    callback()
  })
   /*  files
    * Checks whether the user is logged-in
    * retrieves a list of files previously stored by the user
    * displays the file ids and paths as stored in the database
    */
const files = cli.command('Files')
files
.description('Displays list of files')
.action((args, callback) => {
  if (!Userlogged) {
    cli.log('You are not logged in. You have to be logged in to access')
  } else {
    ServerConnection()
    writeTo(`getlist ${Userlogged}`)
    server.on('data', (d) => {
      cli.log(d.toString())
    })
    DisconnectServer()
    callback()
  }
})
/*
upload <local file path> [path stored in database]
    * Checks whether the user is logged-in
    * selects a local file based on the local file path
    * reads that file and send it to the server
    * optionally allows the user to save the file in the database under a different specified path
*/
const upload = cli.command('upload <localFilePath> [pathStoredInTheDatabase]')
upload
.description('Uploads the local file paths')
.description('Upload a file to you')
  .action((args, callback) => {
    if (!Userlogged) {
      cli.log('You are not logged in. You have to be logged in to access')
    } else {
      let filePathToUpload
      if (!args.pathfordatabase) {
        filePathToUpload = args.absolutefilepath
      } else {
        filePathToUpload = args.pathfordatabase
      }
      ServerConnection()
      fs.open(args.absolutefilepath, 'r', (err, fd) => {
        if (err) {
          cli.log(`There was an error opening the file to send: ${err}`)
        }
        let buffer = new Buffer.alloc(fs.statSync(args.absolutefilepath).size)
        fs.read(fd, buffer, 0, buffer.length, 0, (err, num) => {
          if (err) {
            cli.log(`There was an error opening the file to send: ${err}`)
          }
          writeJSONFile(createFile(filePathToUpload, buffer.toString('base64'), Userlogged))
          if (err) throw err
        })
      })
    }
    DisconnectServer()
    callback()
  })
/*
    * download <database file id> [local file path]
    * requests a file from the server with the specified id
    * by default, it stores that file locally under the path stored in the database
    * the user should be optionally able to specify an alternate local path
*/
const download = cli.command('download <databaseFileId> [localFilePath]')
download
.description('Downloads file from your database')
.action((args, callback) => {
  if (!Userlogged) {
    cli.log('You are not logged in. You have to be logged in to access')
  } else {
    ServerConnection()
    writeTo(`getfile ${args.fileid}`)
    server.on('data', (d) => {
      let filePathToSave
      let parsed = JSON.parse((d.toString()))
      if (!args.filepath) {
        filePathToSave = parsed.files.filePath
      } else {
        filePathToSave = args.filepath
      }
      let buffer = Buffer.from(parsed.files.buffer, 'base64')
      fs.open(filePathToSave, 'w', (err, fd) => {
        if (err) cli.log(`Error opening file to write file: ${err}`)
        fs.write(fd, buffer, 0, buffer.length, null, (err) => {
          if (err) cli.log(`Error writing to file: ${err}`)
          fs.close(fd, () => {
            cli.log(`File written to ${filePathToSave}!`)
          })
        })
      })
    })
  }
  DisconnectServer()
  callback()
})
export default cli
