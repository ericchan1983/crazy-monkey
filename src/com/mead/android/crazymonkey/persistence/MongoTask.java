package com.mead.android.crazymonkey.persistence;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.mead.android.crazymonkey.AndroidEmulator;
import com.mead.android.crazymonkey.CrazyMonkeyBuild;
import com.mead.android.crazymonkey.model.Task;

public class MongoTask implements TaskDAO {

	private static ObjectMapper objectMapper = new ObjectMapper();

	private CrazyMonkeyBuild build;

	
	
	public CrazyMonkeyBuild getBuild() {
		return build;
	}

	public void setBuild(CrazyMonkeyBuild build) {
		this.build = build;
	}

	public MongoTask(CrazyMonkeyBuild build) {
		super();
		this.build = build;
	}

	private String getRequestUrl(String slaverMac, Date date, int numberOfEmulator) {
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String requestUrl = null;
		try {
			
			requestUrl = String.format("%s/task/getnew?slaver.slaverMAC=%s&planExecDate=%s&limit=%d", build.getNodeHttpServer(),
					URLEncoder.encode(slaverMac, StandardCharsets.UTF_8.toString()),
					URLEncoder.encode(format.format(date), StandardCharsets.UTF_8.toString()), numberOfEmulator);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requestUrl;
	}

	public String getRequestUrl(String slaverMac, int numberOfEmulator) {
		String requestUrl = null;
		try {
			requestUrl = String.format("%s/task/getnew?slaver.slaverMAC=%s&limit=%d", build.getNodeHttpServer(),
					URLEncoder.encode(slaverMac, StandardCharsets.UTF_8.toString()), numberOfEmulator);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requestUrl;
	}

	public String getRequestUrl(Date date, int numberOfEmulator) {
		String requestUrl = null;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			requestUrl = String.format("%s/task/getnew?planExecDate=%s", build.getNodeHttpServer(),
					URLEncoder.encode(format.format(date), StandardCharsets.UTF_8.toString()), numberOfEmulator);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return requestUrl;
	}

	public List<Task> getTaskList(String requestUrl) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<Task> taskList = new ArrayList<Task>();
		try {
			HttpGet httpGet = new HttpGet(requestUrl);
			CloseableHttpResponse response = httpclient.execute(httpGet);

			try {
				HttpEntity entity = response.getEntity();

				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					if (entity.getContentLength() > 1) {

						taskList = objectMapper.readValue(entity.getContent(), new TypeReference<List<Task>>() {
						});

						if (taskList != null && !taskList.isEmpty()) {
							for (int i = 0; i < taskList.size(); i++) {
								Task task = taskList.get(i);
								AndroidEmulator emulator = new AndroidEmulator();
								emulator.setAvdName(String.format("%s%d", CrazyMonkeyBuild.EMULATOR_NAME_PREFIX, build.getAvailableEmualtorIndex()));
								task.setEmulator(emulator);
							}
						}
					}
				}
				EntityUtils.consume(entity);
			} finally {
				response.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return taskList;
	}

	@Override
	public List<Task> getTasks(int times, String slaverMac, Date date) {
		// get the tasks by the slaver and the today
		List<Task> taskList = this.getTaskList(this.getRequestUrl(slaverMac, date, times));
		/*
		if (taskList == null || taskList.isEmpty()) {
			// if the tasks of the slaver is empty, get the tasks before in this machine
			taskList = this.getTaskList(this.getRequestUrl(slaverMac, times));
		}
		if (taskList == null || taskList.isEmpty()) {
			// if the tasks of the slaver is empty, get the tasks today from other machine
			taskList = this.getTaskList(this.getRequestUrl(date, times));
		}
		*/
		return taskList;
	}
	
	public boolean updateTask(Task task) {
		boolean isSucess = false;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		try {

			HttpPut putRequest = new HttpPut(String.format("%s/task/%s", build.getNodeHttpServer(), URLEncoder.encode(task.getId(), StandardCharsets.UTF_8.toString())));
			putRequest.addHeader("Accept", "application/json");
			putRequest.addHeader("Content-type", "application/json");
            
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			Map<String, String> valuePair = new HashMap<String, String>();

			if (task.getStatus() != null) {
				valuePair.put("status", task.getStatus().toString());
			}
			if (task.getSlaver() != null && task.getSlaver().getSlaverMAC() != null) {
				valuePair.put("slaver.slaverMAC", task.getSlaver().getSlaverMAC());
			}
			if (task.getAssignTime() != null) {
				valuePair.put("assignTime", format.format(task.getAssignTime()));
			}
			if (task.getExecStartTime() != null) {
				valuePair.put("execStartTime", format.format(task.getExecStartTime()));
			}
			if (task.getExecEndTime() != null) {
				valuePair.put("execEndTime", format.format(task.getExecEndTime()));
			}

			StringEntity input;

			try {
				input = new StringEntity(objectMapper.writeValueAsString(valuePair));
				input.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return isSucess;
			}
			putRequest.setEntity(input);
			response = httpClient.execute(putRequest);
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isSucess;
	}

	@Override
	public boolean resetTask(String slaverMac) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		boolean result = false;
		try {
			String requestUrl = String.format("%s/task/resetAbort?slaverMAC=%s", build.getNodeHttpServer(),
					URLEncoder.encode(slaverMac, StandardCharsets.UTF_8.toString()));

			HttpGet httpGet = new HttpGet(requestUrl);
			response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = true;
			}
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
