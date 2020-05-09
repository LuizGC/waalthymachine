package com.wealthy.machine.core.util.data;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wealthy.machine.core.Config;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;

import java.io.File;
import java.util.Date;
import java.util.Optional;

public class GitVersionControl {
	private final Logger logger;
	private final Git git;
	private final SshTransportConfigCallback sshTransportConfigCallback;

	private class SshTransportConfigCallback implements TransportConfigCallback {

		private final byte[] sshPublicKey;
		private final byte[] sshPrivateKey;
		private final byte[] passphrase;

		private SshTransportConfigCallback(String sshPrivateKey, String sshPublicKey, String passphrase) {
			this.sshPrivateKey = Optional.ofNullable(sshPrivateKey).map(String::getBytes).orElse("".getBytes());
			this.sshPublicKey = Optional.ofNullable(sshPublicKey).map(String::getBytes).orElse("".getBytes());
			this.passphrase = Optional.ofNullable(passphrase).map(String::getBytes).orElse("".getBytes());
		}

		private final SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
			@Override
			protected void configure(OpenSshConfig.Host hc, Session session) {
				session.setConfig("StrictHostKeyChecking", "no");
			}
			@Override
			protected JSch createDefaultJSch(FS fs) throws JSchException {
				JSch jSch = super.createDefaultJSch(fs);
				jSch.addIdentity("GIT_MODE", sshPrivateKey, sshPublicKey, passphrase);
				return jSch;
			}
		};

		@Override
		public void configure(Transport transport) {
			SshTransport sshTransport = (SshTransport) transport;
			sshTransport.setSshSessionFactory(sshSessionFactory);
		}

		public boolean isValid() {
			return this.sshPrivateKey.length > 0 && this.sshPublicKey.length > 0;
		}
	}

	public GitVersionControl(File storageFolder, String branchName, Config config) throws GitAPIException {
		this.logger = config.getLogger(this.getClass());
		var repositoryPath = config.getRepositoryPath();
		var sshPublicKey = config.getGitSshPublicKey();
		var sshPrivateKey = config.getGitSshPrivateKey();
		var passphrase = config.getGitSshPassphrase();
		this.logger.info(System.getenv("GIT_PUBLIC_SSH_KEY"));
		this.sshTransportConfigCallback = new SshTransportConfigCallback(sshPrivateKey, sshPublicKey, passphrase);
		if (sshTransportConfigCallback.isValid()) {
			this.git = Git
					.cloneRepository()
					.setTransportConfigCallback(sshTransportConfigCallback)
					.setURI(repositoryPath)
					.setDirectory(storageFolder)
					.call();
			checkout(branchName);
			logger.info("Saving data on git");
		} else {
			this.git = null;
			logger.info("Not saving data on git");
		}
	}

	public synchronized void push() throws GitAPIException {
		if(this.git != null) {
			logger.info("Uploading data from repository");
			var add = this.git.add();
			addAll(this.git.getRepository().getWorkTree(), add);
			add.call();

			git.commit()
					.setMessage(new Date().toString())
					.call();

			git.push()
					.setTransportConfigCallback(sshTransportConfigCallback)
					.call();
		}
	}

	private void checkout(String branchName) throws GitAPIException {
		var ref = this.git
				.branchList()
				.setListMode(ListMode.REMOTE)
				.call()
				.stream()
				.map(Ref::getName)
				.filter(refName -> refName.contains(branchName))
				.findFirst()
				.orElse("");
		var checkout = this.git
				.checkout()
				.setName(branchName)
				.setCreateBranch(true);
		if(!ref.isBlank()) {
			checkout.setStartPoint(ref);
		}
		checkout.call();
	}

	private void addAll(File file, AddCommand add) {
		if (file.isDirectory()) {
			for (File internalFile : file.listFiles()) {
				if (!internalFile.getName().equals(".git")) {
					addAll(internalFile, add);
				}
			}
		} else {
			String filename = file
					.toString()
					.replace(this.git.getRepository().getWorkTree() + "\\", "")
					.replace("\\", "/");
			add.addFilepattern(filename);
		}
	}
}
